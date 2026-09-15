Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot
function Put($im,$x,$y,$hex){if($x -ge 0 -and $x -lt 16 -and $y -ge 0 -and $y -lt 16){$im.SetPixel($x,$y,[Drawing.ColorTranslator]::FromHtml($hex))}}
for($v=1;$v -le 3;$v++){
$im=[Drawing.Bitmap]::new(16,16);$curve=@(@(3,14),@(3,13),@(3,12),@(4,11),@(4,10),@(5,9),@(6,8),@(7,7),@(8,6))
if($v -eq 2){$curve=@(@(2,14),@(2,13),@(3,12),@(3,11),@(4,10),@(5,9),@(6,8),@(7,7),@(8,6))}
if($v -eq 3){$curve=@(@(4,14),@(4,13),@(4,12),@(4,11),@(5,10),@(5,9),@(6,8),@(7,7),@(8,6))}
foreach($p in $curve){foreach($d in @(@(-1,0),@(1,0),@(0,-1),@(0,1))){Put $im ($p[0]+$d[0]) ($p[1]+$d[1]) '#4B2519'}}
foreach($p in $curve){Put $im $p[0] $p[1] '#FFF2BC';if($p[1] -in 8..12){Put $im ($p[0]+1) $p[1] '#F5A53B'}}
$burst=@(@(10,3),@(8,4),@(10,4),@(12,4),@(9,5),@(10,5),@(11,5),@(7,6),@(8,6),@(9,6),@(10,6),@(11,6),@(12,6),@(9,7),@(10,7),@(11,7),@(8,8),@(10,8),@(12,8),@(10,9))
if($v -eq 2){$burst=@(@(10,3),@(9,4),@(10,4),@(11,4),@(8,5),@(9,5),@(10,5),@(11,5),@(12,5),@(8,6),@(9,6),@(10,6),@(11,6),@(12,6),@(9,7),@(10,7),@(11,7),@(10,8))}
foreach($p in $burst){foreach($d in @(@(-1,0),@(1,0),@(0,-1),@(0,1))){Put $im ($p[0]+$d[0]) ($p[1]+$d[1]) '#591C27'}}
foreach($p in $burst){$hex='#DA3042';if($p[0] -eq 10 -or $p[1] -eq 6){$hex='#FF6663'};Put $im $p[0] $p[1] $hex}
Put $im 10 6 '#FFF2BC';Put $im 9 6 '#FFAD88';Put $im 8 7 '#FFCF54'
$marks=@(@(4,2),@(14,2),@(13,12));if($v -eq 3){$marks=@(@(3,3),@(14,2),@(12,12))}
foreach($p in $marks){foreach($d in @(@(-1,0),@(1,0),@(0,-1),@(0,1))){Put $im ($p[0]+$d[0]) ($p[1]+$d[1]) '#4BD54B'};Put $im $p[0] $p[1] '#C9FF9C'}
$im.Save((Join-Path $root "native/0$v-color.png"));$sil=[Drawing.Bitmap]::new($im);for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){if($sil.GetPixel($x,$y).A -gt 0){Put $sil $x $y '#FFF2BC'}}};$sil.Save((Join-Path $root "native/0$v.png"));$sil.Dispose();$im.Dispose()
}
$b=[Drawing.Bitmap]::new(720,310);$g=[Drawing.Graphics]::FromImage($b);$g.Clear([Drawing.ColorTranslator]::FromHtml('#191C22'));$g.InterpolationMode='NearestNeighbor';$g.PixelOffsetMode='Half';$f=[Drawing.Font]::new('Segoe UI',11)
for($v=1;$v -le 3;$v++){$im=[Drawing.Bitmap]::new((Join-Path $root "native/0$v.png"));$x=20+240*($v-1);$g.DrawString("0$v",$f,[Drawing.Brushes]::White,$x,10);$g.DrawImage($im,[Drawing.Rectangle]::new($x,40,16,16));$g.DrawImage($im,[Drawing.Rectangle]::new($x+45,35,48,48));$g.DrawImage($im,[Drawing.Rectangle]::new($x,95,192,192));$im.Dispose()};$g.Dispose();$b.Save((Join-Path $root 'silhouettes.png'));$b.Dispose()
