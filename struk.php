<?php
    error_reporting(E_ALL ^ (E_NOTICE | E_WARNING));
    require_once "koneksi.php";
    $kode_pj=$_POST['kode_pj'];
?>
<style>
    @media print{
        input.noPrint{
            display: none;
        }
    }
</style>
<table>
    <tr>
        <th align="left">3R COLLECTION</th>
    </tr>

    <tr>
        <td><small>Jl.Raya Rajagaluh - Sumber</small></td>
    </tr>

    <tr>
        <td colspan="1"><small><hr></small></td>
    </tr>
</table>

<table>
    <?php 
        $sql=$koneksi->query("select tb_penjualan.kode_penjualan, tgl_penjualan, tb_pelanggan.nama, tb_pengguna.nama as kasir,nama_barang,harga_jual,jumlah, tb_penjualan_detail.total, diskon,potongan, tb_penjualan.total_b, bayar, kembali from 
        tb_penjualan, tb_penjualan_detail, tb_pelanggan, tb_pengguna, tb_barang 
        where tb_penjualan.id_pelanggan=tb_pelanggan.kode_pelanggan and 
        tb_penjualan.pengguna=tb_pengguna.id and 
        tb_penjualan.kode_penjualan=tb_penjualan_detail.kode_penjualan and 
        tb_penjualan_detail.kode_barang=tb_barang.kode_barang and 
        tb_penjualan_detail.kode_penjualan='$kode_pj'");
        $tampil=$sql->fetch_assoc();
    ?>
    
    <tr>
        <td colspan="2"><small>No. &nbsp&nbsp</small></td>
        <td><small>: &nbsp&nbsp <?php echo $tampil['kode_penjualan']; ?></small></td>
    </tr>

    <tr>
        <td colspan="2"><small>Tanggal &nbsp&nbsp</small></td>
        <td><small>: &nbsp&nbsp <?php echo $tampil['tgl_penjualan']; ?></small></td>
    </tr>

    <tr>
        <td colspan="2"><small>Pelanggan &nbsp&nbsp</small></td>
        <td><small>: &nbsp&nbsp <?php echo $tampil['nama']; ?></small></td>
    </tr>

    <tr>
        <td colspan="2"><small>Kasir &nbsp&nbsp</small></td>
        <td><small>: &nbsp&nbsp <?php echo $tampil['kasir']; ?></small></td>
    </tr>
</table>

<table>
    <tr>
        <td colspan="4"><small><hr></small></td>
    </tr>
    <?php 
        $no=1;
        $sql3=$koneksi->query("select tb_penjualan.kode_penjualan, tgl_penjualan, tb_pelanggan.nama, tb_pengguna.nama as 
        kasir, nama_barang, harga_jual, jumlah, tb_penjualan_detail.total, diskon, potongan, tb_penjualan.total_b, bayar, kembali from 
        tb_penjualan, tb_penjualan_detail, tb_pelanggan, tb_pengguna, tb_barang 
        where tb_penjualan.id_pelanggan=tb_pelanggan.kode_pelanggan and 
        tb_penjualan.pengguna=tb_pengguna.id and 
        tb_penjualan.kode_penjualan=tb_penjualan_detail.kode_penjualan and 
        tb_penjualan_detail.kode_barang=tb_barang.kode_barang and 
        tb_penjualan_detail.kode_penjualan='$kode_pj'");
        while ($tampil3=$sql3->fetch_assoc()) {
    ?>
    <tr>
        <td width="2px"><small><?php echo $no++.'.'; ?></small></td>
        <td><small><?php echo $tampil3['nama_barang']; ?></small></td>
        <td align="right"><small><?php echo number_format($tampil3['harga_jual']).'&nbsp'.'&nbsp'.'x'.'&nbsp'.'&nbsp'.$tampil3['jumlah'].'&nbsp'.'&nbsp'.'&nbsp'.'='.'&nbsp' ?></small></td>
        <td align="right"><small><?php echo number_format($tampil3['total']); ?></small></td>
    </tr>
    <?php
        $diskon=$tampil3['diskon'];
        $potongan=$tampil3['potongan'];
        $bayar=$tampil3['bayar'];
        $kembali=$tampil3['kembali'];
        $total_b=$tampil3['total_b'];
        $total_bayar=$total_bayar+$tampil3['total'];
        } 
    ?>
    <tr>
        <td colspan="4"><hr></td>
    </tr>

    <tr>
        <td colspan="2"></td>
        <td align="right"><small>Total&nbsp&nbsp&nbsp=&nbsp&nbsp&nbsp</small></td>
        <td align="right"><small><?php echo number_format($total_bayar); ?></small></td>
    </tr>

    <tr>
        <td colspan="2"></td>
        <td align="right"><small>Diskon&nbsp&nbsp&nbsp=&nbsp&nbsp&nbsp</small></td>
        <td align="right"><small><?php echo $diskon.'%'; ?></small></td>
    </tr>

    <tr>
        <td colspan="2"></td>
        <td align="right"><small>Potongan&nbsp&nbsp&nbsp=&nbsp&nbsp&nbsp</small></td>
        <td align="right"><small><?php echo number_format($potongan); ?></small></td>
    </tr>

    <tr>
        <td colspan="2"></td>
        <td align="right"><small>Sub Total&nbsp&nbsp&nbsp=&nbsp&nbsp&nbsp</small></td>
        <td align="right"><small><?php echo number_format($total_b); ?></small></td>
    </tr>

    <tr>
        <td colspan="2"></td>
        <td align="right"><small>Bayar&nbsp&nbsp&nbsp=&nbsp&nbsp&nbsp</small></td>
        <td align="right"><small><?php echo number_format($bayar); ?></small></td>
    </tr>

    <tr>
        <td colspan="2"></td>
        <td align="right"><small>Kembali&nbsp&nbsp&nbsp=&nbsp&nbsp&nbsp</small></td>
        <td align="right"><small><?php echo number_format($kembali); ?></small></td>
    </tr>
</table>

<table>
    <tr>
        <br>
        <th><i><small>Note : Barang yang sudah dibeli tidak dapat dikembalikan</small></i></th>
    </tr>
</table>

<br>
<input type="button" class="noPrint" value="Print" onclick="window.print()">